cmake_minimum_required(VERSION 3.4.1)

project("noise")

add_library(
        native-lib
        SHARED
        src/main/cpp/native-lib.cpp )

include_directories(src/main/cpp/)

find_library(
        log-lib
        log )

target_link_libraries(
        native-lib
        ${log-lib} )