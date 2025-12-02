package org.example.webfluxlearning.base.request;

public interface TokenKeyManager<T> {
    public T generateSecretKey();
    public T getActivateKey();
    public Boolean rotateKey(String key);
}
