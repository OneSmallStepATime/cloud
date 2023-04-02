package com.lisihong.manager;

import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.LifecycleState;
import org.apache.catalina.Session;
import org.apache.catalina.session.ManagerBase;

public class NoSessionManager extends ManagerBase implements Lifecycle {
    @Override
    protected synchronized void startInternal() throws LifecycleException {
        super.startInternal();
        setState(LifecycleState.STARTING);
    }
    @Override
    protected synchronized void stopInternal() throws LifecycleException {
        setState(LifecycleState.STOPPING);
        super.stopInternal();
    }
    @Override
    public void load() {
    }
    @Override
    public void unload() {
    }
    @Override
    public Session createSession(String sessionId) {
        return null;
    }
    @Override
    public Session createEmptySession() {
        return null;
    }
    @Override
    public void add(Session session) {
    }
    @Override
    public Session findSession(String id) {
        return null;
    }
    @Override
    public Session[] findSessions() {
        return null;
    }
    @Override
    public void processExpires() {
    }
}