from rest_framework import serializers
from .models import Members

# serializer는 JSON 형태로 바꿔주는 것??


class MembersSerializer(serializers.ModelSerializer):
    class Meta:
        model = Members
        fields = ["user_id", "pw", "phone_number", "created"]
