package ru.tr1al.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SynchronizeUtil {

    public static class ObjectLock {

        public final Lock lock = new ReentrantLock();
        private final Condition removed = lock.newCondition();
        public final Set<Integer> objects = new TreeSet<>();

        public void lock(Integer id) {
            try {
                lock.lock();
                while (objects.contains(id)) {
                    removed.await();
                }
                objects.add(id);
            } catch (Exception e) {
            } finally {
                lock.unlock();
            }
        }

        public void unlock(Integer id) {
            try {
                lock.lock();
                objects.remove(id);
                removed.signalAll();
            } finally {
                lock.unlock();
            }
        }

        public boolean isLocked(Integer id) {
            try {
                lock.lock();
                return objects.contains(id);
            } finally {
                lock.unlock();
            }
        }
    }

    public static class ObjectLockWithReason {

        private final Lock lock = new ReentrantLock();
        private final Condition removed = lock.newCondition();
        private final Map<Integer, String> objects = new HashMap<>();

        public void lock(Integer id, String reason) {
            try {
                lock.lock();
                while (objects.containsKey(id)) {
                    removed.await();
                }
                objects.put(id, reason);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }

        public void unlock(Integer id) {
            try {
                lock.lock();
                objects.remove(id);
                removed.signal();
            } finally {
                lock.unlock();
            }
        }
    }
}
