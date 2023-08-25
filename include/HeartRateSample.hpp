#pragma once
#include "CLAID.hpp"

namespace claid
{
    struct HeartRateSample
    {
        int32_t hr;
        int32_t hrIbi;
        int32_t status;

        Reflect(HeartRateSample,
            reflectMember(hr);
            reflectMember(hrIbi);
            reflectMember(status);
        )
    };
}