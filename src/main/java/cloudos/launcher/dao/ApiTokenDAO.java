package cloudos.launcher.dao;

import cloudos.launcher.model.LaunchAccount;
import cloudos.model.auth.ApiToken;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Repository @Slf4j
public class ApiTokenDAO {

    // sessions are kept in memory
    private Cache<String, LaunchAccount> sessionCache = CacheBuilder.newBuilder()
            .concurrencyLevel(4)
            .maximumSize(100)
            .expireAfterWrite(30, TimeUnit.MINUTES)
            .expireAfterAccess(30, TimeUnit.MINUTES)
            .build();

    public LaunchAccount findAccount(String token) {
        return sessionCache.getIfPresent(token);
    }

    public ApiToken createSession (LaunchAccount account) {
        final String key = UUID.randomUUID().toString();
        sessionCache.put(key, account);
        return new ApiToken(key);
    }

}
