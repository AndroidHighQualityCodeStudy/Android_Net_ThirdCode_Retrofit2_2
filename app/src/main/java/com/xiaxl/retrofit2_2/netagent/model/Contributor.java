package com.xiaxl.retrofit2_2.netagent.model;


public class Contributor {
    public String login;
    public Integer contributions;

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("login: ");
        sb.append(login);
        sb.append(" contributions: ");
        sb.append(contributions);
        return sb.toString();
    }
}
