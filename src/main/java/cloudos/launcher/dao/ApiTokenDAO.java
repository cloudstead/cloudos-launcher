package cloudos.launcher.dao;

import cloudos.launcher.model.LaunchAccount;
import cloudos.model.auth.ApiToken;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.cobbzilla.util.daemon.ZillaRuntime.empty;

@Repository @Slf4j
public class ApiTokenDAO {

    // sessions are kept in memory
    private Cache<String, LaunchAccount> sessionCache = buildCache();
    private Cache<LaunchAccount, List<String>> reverseCache = buildCache();

    private <K, V> Cache<K, V> buildCache() {
        return CacheBuilder.newBuilder()
                .concurrencyLevel(4)
                .maximumSize(100)
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .expireAfterAccess(30, TimeUnit.MINUTES)
                .build();
    }

    public LaunchAccount findAccount(String token) {
        return sessionCache.getIfPresent(token);
    }

    public ApiToken createSession (LaunchAccount account) {
        final String key = UUID.randomUUID().toString();
        sessionCache.put(key, account);

        List<String> tokens = reverseCache.getIfPresent(account);
        if (tokens == null) {
            tokens = new ArrayList<>();
            reverseCache.put(account, tokens);
        }
        tokens.add(key);

        return new ApiToken(key);
    }

    public void invalidateAll (LaunchAccount account) {
        List<String> tokens = reverseCache.getIfPresent(account);
        if (!empty(tokens)) {
            for (String token : tokens) {
                sessionCache.invalidate(token);
            }
        }
        reverseCache.invalidate(account);
    }

}
