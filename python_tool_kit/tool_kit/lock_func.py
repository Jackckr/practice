# -*- coding: utf-8 -*-

from threading import Lock, RLock
import functools
from time import sleep
import threading

# 锁装饰器理解，在程序启动的时候，func函数就会被封装成inner_wrapper，然后当调用func的时候，实际调用的是inner_wrapper函数
def lock(read_lock):
    def wrapper(func):
        if read_lock:
            func.__lock = RLock()
        else:
            func.__lock = Lock()
        @functools.wraps(func)
        def inner_wrapper(*args, **kwargs):
            with func.__lock:
                return func(*args, **kwargs)

        return inner_wrapper

    return wrapper


def synchronized(func):
    return lock(False)(func)


def singleton(cls):
    instances = {}

    @synchronized
    def get_instance():
        if cls not in instances:
            instances[cls] = cls()
        return instances[cls]

    return get_instance

a = 0

@synchronized
def lock_test():
    global a
    a = a + 1
    count = 0
    while count < 10:
        print a
        count = count + 1
        sleep(1)

class lock_class_test():

    def __init__(self, a):
        self._a = a

    @synchronized
    def lock_test(self):
        count = 0
        while count < 10:
            count = count + 1
            print self._a
            sleep(1)


if __name__ == '__main__':
    t = threading.Thread(name="thread",target=lock_test)
    t.start()
    lock_test()

    test1 = lock_class_test("test1")
    t = threading.Thread(name="thread",target=test1.lock_test)
    t.start()
    test1.lock_test()

    test2 = lock_class_test("test2")
    t = threading.Thread(name="thread",target=test2.lock_test)
    t.start()
    test1.lock_test()