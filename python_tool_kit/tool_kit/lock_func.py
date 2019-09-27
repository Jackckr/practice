# -*- coding: utf-8 -*-

from threading import Lock, RLock
import functools


def lock(read_lock):
    def wrapper(func):
        @functools.wraps(func)
        def inner_wrapper(*args, **kwargs):
            if read_lock:
                func.__lock = RLock()
            else:
                func.__lock = Lock()
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