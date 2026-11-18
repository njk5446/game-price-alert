package com.dymun.pricealert.domain.notification;

// 알림 방식 인터페이스
// 알림을 "어떻게" 보낼지(이메일/문자/카톡)는 구현체가 알아서 하고,
// 이 인터페이스만 보고 호출하면 나중에 채널이 추가돼도 NotificationService 코드는 안 바꿔도 됨
public interface Notifier {
    // 전송
    void send(String to, String subject, String text);
}
