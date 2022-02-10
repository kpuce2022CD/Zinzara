"""zinzaraServer URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/3.1/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path
from django.conf.urls import url, include
from django.contrib.auth.models import User
from rest_framework import routers, serializers, viewsets
#from members import Mviews
import members.views as Mviews
#from devices import Dviews
import devices.views as Dviews
#from rehabilitation import Rviews
import rehabilitation.views as Rviews
import gestures.views as Gviews



urlpatterns = [
    path("zinzara/", Mviews.hello),
    path('admin/', admin.site.urls),
    path('api-auth', include("rest_framework.urls", namespace="rest_framework")),
    path('members/', Mviews.members),   # 사용자 추가하기
    path('members-info/', Mviews.members_info),   # 사용자 삭제하기, 정보 수정하기, 정보 가져오기
    path("login/", Mviews.login),   # 로그인
    path("devices/", Dviews.devices),   # 기기 명령하기
    path("devices-info/", Dviews.devices_info),   # 기기 정보 가져오기, 기기 삭제하기
    path("language-rehabilitation/", Rviews.language_rehabilitation),
    path("physical-rehabilitation/", Rviews.physical_rehabilitation),
    path("gestures/", Gviews.gestures),
]
