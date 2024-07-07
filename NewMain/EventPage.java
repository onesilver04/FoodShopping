import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EventPage {
    // 전역 변수 선언
    private static JPanel calendarPanel;
    private static JLabel monthLabel;
    private static LocalDate currentMonth;

    public static JPanel createEventPage() {
        // 전역 변수 초기화
        JPanel mainPanel = new JPanel();
        calendarPanel = new JPanel(new GridLayout(0, 7)) {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.LIGHT_GRAY);
                int y = 0;
                int rowHeight = getHeight() / getRowCount();
                for (int i = 1; i < getRowCount(); i++) {
                    y += rowHeight;
                    g.drawLine(0, y, getWidth(), y);
                }
            }

            private int getRowCount() {
                int totalCells = 7 + currentMonth.lengthOfMonth();
                int dayOfWeek = currentMonth.withDayOfMonth(1).getDayOfWeek().getValue() % 7;
                totalCells += dayOfWeek;
                return (int) Math.ceil(totalCells / 7.0);
            }
        };
        monthLabel = new JLabel();
        currentMonth = LocalDate.of(2024, 7, 1); // 2024년 7월 1일을 기준으로 설정

        // 레이아웃 설정
        mainPanel.setLayout(new BorderLayout());

        // 월 변경 패널
        JPanel monthPanel = new JPanel();
        JButton prevMonthButton = new JButton("<");
        JButton nextMonthButton = new JButton(">");
        prevMonthButton.addActionListener(e -> changeMonth(-1));
        nextMonthButton.addActionListener(e -> changeMonth(1));
        monthPanel.add(prevMonthButton);
        monthPanel.add(monthLabel);
        monthPanel.add(nextMonthButton);

        // 달력 패널
        updateCalendar();

        // 메인 패널에 추가
        mainPanel.add(monthPanel, BorderLayout.NORTH);
        mainPanel.add(calendarPanel, BorderLayout.CENTER);

        return mainPanel;
    }

    // 달력 업데이트 메서드
    private static void updateCalendar() {
        calendarPanel.removeAll();
        monthLabel.setText(currentMonth.format(DateTimeFormatter.ofPattern("yyyy.MM")));

        // 요일 헤더 추가
        String[] days = {"일", "월", "화", "수", "목", "금", "토"};
        for (String day : days) {
            JLabel dayLabel = new JLabel(day, SwingConstants.CENTER);
            dayLabel.setOpaque(true);
            dayLabel.setBackground(Color.LIGHT_GRAY);
            calendarPanel.add(dayLabel);
        }

        // 빈 날짜 채우기
        LocalDate firstDayOfMonth = currentMonth.withDayOfMonth(1);
        int dayOfWeek = firstDayOfMonth.getDayOfWeek().getValue();
        dayOfWeek = dayOfWeek % 7; // 일요일부터 시작하도록 조정
        for (int i = 0; i < dayOfWeek; i++) {
            JLabel emptyLabel = new JLabel("");
            calendarPanel.add(emptyLabel);
        }

        // 날짜 추가
        int daysInMonth = currentMonth.lengthOfMonth();
        LocalDate eventStartDate = LocalDate.of(2024, 7, 10);
        LocalDate eventEndDate = LocalDate.of(2024, 7, 15);
        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate currentDate = currentMonth.withDayOfMonth(day);
            DayLabel dayLabel = new DayLabel(day, currentDate, eventStartDate, eventEndDate);
            calendarPanel.add(dayLabel);

            // 툴팁 추가
            if (currentDate.getDayOfMonth() == 1) {
                dayLabel.setToolTipText("<html>정기 이벤트 :<br> 매달 첫째 날에 선착순 10명에게 5% 할인 쿠폰 증정</html>");
            } else if (!currentDate.isBefore(eventStartDate) && !currentDate.isAfter(eventEndDate)) {
                dayLabel.setToolTipText("<html>7/10-7/15 여름 맞이 세일 이벤트 :<br>여름 맞이 수박 및 복숭아 할인 이벤트</html>");

            }
        }

        // 빈 셀 추가
        int totalCells = dayOfWeek + daysInMonth;
        int rows = (int) Math.ceil(totalCells / 7.0);
        for (int i = totalCells; i < rows * 7; i++) {
            JLabel emptyLabel = new JLabel("");
            calendarPanel.add(emptyLabel);
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    // 월 변경 메서드
    private static void changeMonth(int months) {
        currentMonth = currentMonth.plusMonths(months);
        updateCalendar();
    }

    // 커스텀 JLabel 클래스
    static class DayLabel extends JLabel {
        private LocalDate currentDate;
        private LocalDate eventStartDate;
        private LocalDate eventEndDate;

        public DayLabel(int day, LocalDate currentDate, LocalDate eventStartDate, LocalDate eventEndDate) {
            super(String.valueOf(day));
            this.currentDate = currentDate;
            this.eventStartDate = eventStartDate;
            this.eventEndDate = eventEndDate;
            setHorizontalAlignment(SwingConstants.CENTER);
            setVerticalAlignment(SwingConstants.TOP);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (currentDate.getDayOfMonth() == 1) {
                g.setColor(new Color(144, 238, 144)); // 초록색으로 변경
                g.fillRect(0, getHeight() / 2, getWidth(), getHeight() / 2); // 칸의 하단 절반만 색칠
				g.setColor(Color.BLACK);
                g.setFont(new Font("Dialog", Font.BOLD, 11)); // 기본 폰트 사용
                FontMetrics fm = g.getFontMetrics();
                String eventText1 = "1일 정기 이벤트";
                int textWidth1 = fm.stringWidth(eventText1);
                int x1 = (getWidth() - textWidth1) / 2;
                g.drawString(eventText1, x1, getHeight() - fm.getHeight() * 2);
            } else if (!currentDate.isBefore(eventStartDate) && !currentDate.isAfter(eventEndDate)) {
                g.setColor(new Color(173, 216, 230)); // 부드러운 색상으로 변경
                g.fillRect(0, getHeight() / 2, getWidth(), getHeight() / 2); // 칸의 하단 절반만 색칠
                g.setColor(Color.BLACK);
                if (currentDate.equals(eventStartDate)) {
                    g.setFont(new Font("Dialog", Font.BOLD, 11)); // 기본 폰트 사용
                    FontMetrics fm = g.getFontMetrics();
                    String eventText1 = "7/10-7/15";
                    String eventText2 = "여름 맞이 세일 이벤트";
                    int textWidth1 = fm.stringWidth(eventText1);
                    int textWidth2 = fm.stringWidth(eventText2);
                    int x1 = (getWidth() - textWidth1) / 2;
                    int x2 = (getWidth() - textWidth2) / 2;
                    g.drawString(eventText1, x1, getHeight() - fm.getHeight() * 2);
                    g.drawString(eventText2, x2, getHeight() - fm.getHeight());
                }
            }
        }
    }
}
