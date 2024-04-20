#!/bin/sh

docker run --rm -u "$(id -u)" -v"${PWD}":"${PWD}" -w"${PWD}" jaegertracing/protobuf:latest --proto_path="${PWD}" --java_out="${PWD}" -I/usr/include/github.com/gogo/protobuf "${PWD}/Mumble.proto"
mv MumbleProto ../java/

docker run --rm -u "$(id -u)" -v"${PWD}":"${PWD}" -w"${PWD}" jaegertracing/protobuf:latest --proto_path="${PWD}" --java_out="${PWD}" -I/usr/include/github.com/gogo/protobuf "${PWD}/MumbleUDP.proto"
mv MumbleUDP ../java/

