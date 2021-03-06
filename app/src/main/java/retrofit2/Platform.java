/*
 * Copyright (C) 2013 Square, Inc.
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

import android.os.Build;
import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

/**
 * 平台获取
 */
class Platform {

    // 单例
    private static final Platform PLATFORM = findPlatform();

    static Platform get() {
        return PLATFORM;
    }

    /**
     * 获取Android 或者 Java 平台
     *
     * @return
     */
    private static Platform findPlatform() {
        try {
            Class.forName("android.os.Build");
            if (Build.VERSION.SDK_INT != 0) {
                return new Android();
            }
        } catch (ClassNotFoundException ignored) {
        }
//    try {
//      Class.forName("java.util.Optional");
//      return new Java8();
//    } catch (ClassNotFoundException ignored) {
//    }
        return new Platform();
    }

    Executor defaultCallbackExecutor() {
        return null;
    }

    CallAdapter.Factory defaultCallAdapterFactory(Executor callbackExecutor) {
        if (callbackExecutor != null) {
            return new ExecutorCallAdapterFactory(callbackExecutor);
        }
        return DefaultCallAdapterFactory.INSTANCE;
    }

    boolean isDefaultMethod(Method method) {
        return false;
    }

    Object invokeDefaultMethod(Method method, Class<?> declaringClass, Object object, Object... args)
            throws Throwable {
        throw new UnsupportedOperationException();
    }

//  @IgnoreJRERequirement // Only classloaded and used on Java 8.
//  static class Java8 extends Platform {
//    @Override boolean isDefaultMethod(Method method) {
//      return method.isDefault();
//    }
//
//    @Override Object invokeDefaultMethod(Method method, Class<?> declaringClass, Object object,
//        Object... args) throws Throwable {
//      // Because the service interface might not be public, we need to use a MethodHandle lookup
//      // that ignores the visibility of the declaringClass.
//      Constructor<Lookup> constructor = Lookup.class.getDeclaredConstructor(Class.class, int.class);
//      constructor.setAccessible(true);
//      return constructor.newInstance(declaringClass, -1 /* trusted */)
//          .unreflectSpecial(method, declaringClass)
//          .bindTo(object)
//          .invokeWithArguments(args);
//    }
//  }

    /**
     * Android 平台
     */
    static class Android extends Platform {

        /**
         * 将Runable post到主线程的线程池
         *
         * @return
         */
        @Override
        public Executor defaultCallbackExecutor() {
            return new MainThreadExecutor();
        }

        /**
         * 传入一个对应的线程池
         *
         * @param callbackExecutor
         * @return
         */
        @Override
        CallAdapter.Factory defaultCallAdapterFactory(Executor callbackExecutor) {
            return new ExecutorCallAdapterFactory(callbackExecutor);
        }


        static class MainThreadExecutor implements Executor {
            // 主线程的Handler
            private final Handler handler = new Handler(Looper.getMainLooper());

            /**
             * Runable post到主线程
             *
             * @param r
             */
            @Override
            public void execute(Runnable r) {
                handler.post(r);
            }
        }
    }
}
