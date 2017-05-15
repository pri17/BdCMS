package com.bidanet.bdcms.service;

import com.bidanet.bdcms.entity.EventHook;

import java.util.Map;

/**
 *
 */
public interface EventHookService extends Service<EventHook> {

    void hookT(String event_code, Map<String, Object> param);
}
