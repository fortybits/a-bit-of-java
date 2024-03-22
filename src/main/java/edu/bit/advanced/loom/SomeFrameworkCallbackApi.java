package edu.bit.advanced.loom;

public interface SomeFrameworkCallbackApi {
    void beforeRequest(RequestHeaders req, ConfigInfo info);

    void afterRequest();
}
