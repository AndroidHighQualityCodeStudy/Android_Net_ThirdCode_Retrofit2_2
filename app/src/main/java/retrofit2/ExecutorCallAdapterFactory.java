/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package retrofit2;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.Executor;

import okhttp3.Request;

import static retrofit2.Utils.checkNotNull;

final class ExecutorCallAdapterFactory extends CallAdapter.Factory {

    // 线程池对象
    final Executor callbackExecutor;

    ExecutorCallAdapterFactory(Executor callbackExecutor) {
        this.callbackExecutor = callbackExecutor;
    }

    /**
     * @param returnType  返回类型
     * @param annotations 注解
     * @param retrofit
     * @return
     */
    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        //
        if (getRawType(returnType) != Call.class) {
            return null;
        }
        // ??????????????????????
        final Type responseType = Utils.getCallResponseType(returnType);
        return new CallAdapter<Object, Call<?>>() {
            @Override
            public Type responseType() {
                return responseType;
            }

            /**
             *
             * @param call OkHttpCall
             * @return
             */
            @Override
            public Call<Object> adapt(Call<Object> call) {
                return new ExecutorCallbackCall<>(callbackExecutor, call);
            }
        };
    }

    static final class ExecutorCallbackCall<T> implements Call<T> {
        final Executor callbackExecutor;
        // OkHttpCall
        final Call<T> delegate;

        /**
         * @param callbackExecutor
         * @param delegate         OkHttpCall
         */
        ExecutorCallbackCall(Executor callbackExecutor, Call<T> delegate) {
            this.callbackExecutor = callbackExecutor;
            // OkHttpCall
            this.delegate = delegate;
        }

        @Override
        public void enqueue(final Callback<T> callback) {
            checkNotNull(callback, "callback == null");
            // OkHttpCall.enqueue
            delegate.enqueue(new Callback<T>() {
                @Override
                public void onResponse(Call<T> call, final Response<T> response) {

                    // 创建一个Runable 将Response结果回调到callbackExecutor对应的线程池中
                    callbackExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            // 请求被取消
                            if (delegate.isCanceled()) {
                                // Emulate OkHttp's behavior of throwing/delivering an IOException on cancellation.
                                callback.onFailure(ExecutorCallbackCall.this, new IOException("Canceled"));
                            } else {
                                callback.onResponse(ExecutorCallbackCall.this, response);
                            }
                        }
                    });
                }

                @Override
                public void onFailure(Call<T> call, final Throwable t) {
                    // 创建一个Runable 将Response结果回调到callbackExecutor对应的线程池中
                    callbackExecutor.execute(new Runnable() {
                        @Override
                        public void run() {
                            // 创建一个Runable 将Response结果回调到callbackExecutor对应的线程池中
                            callback.onFailure(ExecutorCallbackCall.this, t);
                        }
                    });
                }
            });
        }

        @Override
        public boolean isExecuted() {
            return delegate.isExecuted();
        }

        @Override
        public Response<T> execute() throws IOException {
            return delegate.execute();
        }

        @Override
        public void cancel() {
            delegate.cancel();
        }

        @Override
        public boolean isCanceled() {
            return delegate.isCanceled();
        }

        @SuppressWarnings("CloneDoesntCallSuperClone") // Performing deep clone.
        @Override
        public Call<T> clone() {
            return new ExecutorCallbackCall<>(callbackExecutor, delegate.clone());
        }

        @Override
        public Request request() {
            return delegate.request();
        }
    }
}
