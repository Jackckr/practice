# -*- coding: utf-8 -*-

from threading import local
import functools

thread_local = local()

def force_master():
    thread_local.force_master = True


def is_force_master():
    return thread_local.__dict__.get("force_master", False)


def clear_force_master():
    thread_local.force_master = False


def force_master_decorator(func):
    @functools.wraps(func)
    def wrapper(*args, **kwargs):
        try:
            force_master()
            return func(*args, **kwargs)
        finally:
            clear_force_master()

    return wrapper