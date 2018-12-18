"""wall URL Configuration"""
from django.urls import path, include
from rest_framework.routers import DefaultRouter

from posts import views as post_views
from users import views as user_views

ROUTER = DefaultRouter()
ROUTER.register(r'posts', post_views.PostViewSet)
ROUTER.register(r'users', user_views.UserViewSet)

urlpatterns = [
    path('', include(ROUTER.urls)),
]
