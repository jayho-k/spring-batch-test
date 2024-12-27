package com.example.Partition_Test.ChunkTest.config.tenant;

import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.initialization.qual.Initialized;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.checker.nullness.qual.UnknownKeyFor;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Map;

@Slf4j
@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver, HibernatePropertiesCustomizer {

    private final ThreadLocal<String> currentTenant = ThreadLocal.withInitial(() -> "spring_batch");

    public void setCurrentTenant(String tenant){
        currentTenant.set(tenant.toLowerCase().trim());
        log.info(">>>>>>>> {} <<<<<<<<< currentTenant 설정 완료", currentTenant.get());
    }
    public void removeCurrentTenant() {
        currentTenant.remove();
        log.info(">>>>>>>> {} <<<<<<<<< currentTenant 해제 완료", currentTenant.get());
    }

    private void checkThreadLocal() throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException {
        Thread currentThread = Thread.currentThread();

        // Thread의 threadLocals 필드에 접근
        Field threadLocalsField = Thread.class.getDeclaredField("threadLocals");
        threadLocalsField.setAccessible(true); // private 필드 접근 허용

        // 현재 쓰레드의 threadLocals 값 가져오기
        Object threadLocalMap = threadLocalsField.get(currentThread);
        System.out.println("ThreadLocalMap: " + threadLocalMap);

        // ThreadLocalMap의 Entry 배열에 접근하기
        Class<?> threadLocalMapClass = Class.forName("java.lang.ThreadLocal$ThreadLocalMap");

        Field tableField = threadLocalMapClass.getDeclaredField("table");
        tableField.setAccessible(true);

        Object[] table = (Object[]) tableField.get(threadLocalMap);

        for (Object entry : table) {
            if (entry != null) {
                // Entry의 key (ThreadLocal)와 value에 접근
                Field keyField = entry.getClass().getDeclaredField("referent");
                keyField.setAccessible(true);
                ThreadLocal<?> key = (ThreadLocal<?>) keyField.get(entry);

                Field valueField = entry.getClass().getDeclaredField("value");
                valueField.setAccessible(true);
                Object value = valueField.get(entry);

                System.out.println("Key: " + key + ", Value: " + value);
            }
        }
    }

    @Override
    public @UnknownKeyFor @Nullable @Initialized Object resolveCurrentTenantIdentifier() {
        log.info(">>>>>>>> {} <<<<<<<<< resolveCurrentTenantIdentifier ", currentTenant.get());
        return currentTenant.get();
    }

    @Override
    public @UnknownKeyFor @NonNull @Initialized boolean validateExistingCurrentSessions() {
        return false;
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, this);
    }
}
