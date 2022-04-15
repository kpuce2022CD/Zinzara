from rest_framework import serializers
from .models import Devices


class DevicesSerializer(serializers.ModelSerializer):
    class Meta:
        model = Devices
        fields = ["user_id", "pw", "device_name", "device_command", "device_command_time"]

