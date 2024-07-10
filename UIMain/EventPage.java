import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class EventPage {
    // 전역 변수 선언
    private static JPanel calendarPanel;
    private static JLabel monthLabel;
    private static JLabel dateTimeLabel; // 현재 날짜와 시간을 표시할 레이블 추가
    private static LocalDate currentMonth;
    private static LocalDate today; // 오늘 날짜를 저장하는 변수 추가

    public static JPanel createEventPage() {
        // 전역 변수 초기화
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.WHITE); // 메인 패널 배경색 설정
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
        calendarPanel.setBackground(Color.WHITE); // 캘린더 패널 배경색 설정

        monthLabel = new JLabel();
        dateTimeLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 둥근 모서리 사각형 그리기
                g.setColor(Color.LIGHT_GRAY);
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 16);
                super.paintComponent(g);
            }
        };
        dateTimeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dateTimeLabel.setOpaque(false); // 배경을 투명하게 설정

        currentMonth = LocalDate.of(2024, 7, 1); // 2024년 7월 1일을 기준으로 설정
        today = LocalDate.now(); // 오늘 날짜를 가져옴

        // 레이아웃 설정
        mainPanel.setLayout(new BorderLayout());

        // 상단 패널 (월 변경 패널과 날짜 및 시간 패널 포함)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE); // 상단 패널 배경색 설정

        // 월 변경 패널
        JPanel monthPanelWrapper = new JPanel(new GridBagLayout());
        monthPanelWrapper.setBackground(Color.WHITE); // 월 변경 패널 배경색 설정
        JPanel monthPanel = new JPanel();
        monthPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 0));
        monthPanel.setBackground(Color.WHITE); // 월 변경 패널 배경색 설정
        JButton prevMonthButton = new JButton("<");
        JButton nextMonthButton = new JButton(">");
        prevMonthButton.setPreferredSize(new Dimension(40, 25)); // 버튼 크기 설정
        nextMonthButton.setPreferredSize(new Dimension(40, 25)); // 버튼 크기 설정
        prevMonthButton.setBackground(new Color(183, 240, 177)); // 버튼 색상 설정
        nextMonthButton.setBackground(new Color(183, 240, 177)); // 버튼 색상 설정
        prevMonthButton.setFont(new Font("Arial", Font.PLAIN, 12)); // 글자 크기 설정
        nextMonthButton.setFont(new Font("Arial", Font.PLAIN, 12)); // 글자 크기 설정
        prevMonthButton.setMargin(new Insets(0, 0, 0, 0)); // 내부 여백 설정
        nextMonthButton.setMargin(new Insets(0, 0, 0, 0)); // 내부 여백 설정
        prevMonthButton.addActionListener(e -> changeMonth(-1));
        nextMonthButton.addActionListener(e -> changeMonth(1));
        monthPanel.add(prevMonthButton);
        monthPanel.add(monthLabel);
        monthPanel.add(nextMonthButton);
        monthPanelWrapper.add(monthPanel);

        // 날짜와 시간 패널
        JPanel dateTimePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // 둥근 모서리 사각형 그리기
                g.setColor(Color.LIGHT_GRAY);
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
				
            }
        };
        dateTimePanel.setBackground(Color.WHITE); // 날짜와 시간 패널 배경색 설정
        dateTimePanel.add(dateTimeLabel);

        // 상단 패널에 월 변경 패널과 날짜 및 시간 패널 추가
        topPanel.add(monthPanelWrapper, BorderLayout.CENTER);
        topPanel.add(dateTimePanel, BorderLayout.EAST);

        // 달력 패널
        updateCalendar();

        // 메인 패널에 추가
        mainPanel.add(topPanel, BorderLayout.NORTH); // 상단 패널을 메인 패널의 북쪽에 추가
        mainPanel.add(calendarPanel, BorderLayout.CENTER);

        // 현재 날짜와 시간을 업데이트하는 쓰레드 시작
        startDateTimeUpdater();

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
            dayLabel.setBackground(new Color(183, 240, 177));
            dayLabel.setPreferredSize(new Dimension(0, 20)); // 요일 헤더의 높이 설정
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
        LocalDate eventStartDate = LocalDate.of(2024, 8, 1);
        LocalDate eventEndDate = LocalDate.of(2024, 8, 15);
        LocalDate julyEventStartDate = LocalDate.of(2024, 7, 1); // 7월 1일 이벤트 시작
        LocalDate julyEventEndDate = LocalDate.of(2024, 7, 15); // 7월 15일 이벤트 종료

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate currentDate = currentMonth.withDayOfMonth(day);
            DayLabel dayLabel = new DayLabel(day, currentDate, eventStartDate, eventEndDate, julyEventStartDate, julyEventEndDate);
            calendarPanel.add(dayLabel);

            // 툴팁 추가
            if (currentDate.getDayOfMonth() == 28) {
                dayLabel.setToolTipText("<html>정기 이벤트 :<br> 매달 28일 선착순 10명에게 5% 할인 쿠폰 증정</html>");
            } else if (!currentDate.isBefore(eventStartDate) && !currentDate.isAfter(eventEndDate)) {
                dayLabel.setToolTipText("<html>8/1-8/15 여름 맞이 세일 대전 :<br>전 상품 50% 할인 이벤트</html>");
            } else if (!currentDate.isBefore(julyEventStartDate) && !currentDate.isAfter(julyEventEndDate)) {
                dayLabel.setToolTipText("<html>7/1-7/15 명품김치 세일 대전 :<br>김치 상품 40% 할인</html>");
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

    // 현재 날짜와 시간을 업데이트하는 쓰레드 시작 메서드
    private static void startDateTimeUpdater() {
        Thread dateTimeThread = new Thread(() -> {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            while (true) {
                String currentDateTime = LocalDateTime.now().format(dateTimeFormatter);
                SwingUtilities.invokeLater(() -> dateTimeLabel.setText(currentDateTime));
                try {
                    Thread.sleep(1000); // 1초마다 업데이트
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        dateTimeThread.setDaemon(true); // 메인 쓰레드 종료 시 함께 종료
        dateTimeThread.start();
    }

    // 커스텀 JLabel 클래스
    static class DayLabel extends JLabel {
        private LocalDate currentDate;
        private LocalDate eventStartDate;
        private LocalDate eventEndDate;
        private LocalDate julyEventStartDate;
        private LocalDate julyEventEndDate;

        public DayLabel(int day, LocalDate currentDate, LocalDate eventStartDate, LocalDate eventEndDate, LocalDate julyEventStartDate, LocalDate julyEventEndDate) {
            super(String.valueOf(day));
            this.currentDate = currentDate;
            this.eventStartDate = eventStartDate;
            this.eventEndDate = eventEndDate;
            this.julyEventStartDate = julyEventStartDate;
            this.julyEventEndDate = julyEventEndDate;
            setHorizontalAlignment(SwingConstants.CENTER);
            setVerticalAlignment(SwingConstants.TOP);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (currentDate.equals(today)) { // 오늘 날짜인 경우
                setForeground(Color.BLUE);
                setFont(getFont().deriveFont(Font.BOLD));
                setOpaque(true);
              // 배경색을 노란색으로 설정
            } else {
                setOpaque(false);
            }

            if (currentDate.getDayOfMonth() == 28) {
                g.setColor(new Color(144, 238, 144)); // 초록색으로 변경
                g.fillRect(0, getHeight() / 2, getWidth(), getHeight() / 2); // 칸의 하단 절반만 색칠
                g.setColor(Color.BLACK);
                g.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 11)); // 기본 폰트 사용
                FontMetrics fm = g.getFontMetrics();
                String eventText1 = "28일 정기 이벤트";
                int textWidth1 = fm.stringWidth(eventText1);
                int x1 = (getWidth() - textWidth1) / 2;
                g.drawString(eventText1, x1, getHeight() - fm.getHeight() * 2);
            } else if (!currentDate.isBefore(eventStartDate) && !currentDate.isAfter(eventEndDate)) {
                g.setColor(new Color(173, 216, 230)); // 부드러운 색상으로 변경
                g.fillRect(0, getHeight() / 2, getWidth(), getHeight() / 2); // 칸의 하단 절반만 색칠
                g.setColor(Color.BLACK);
                if (currentDate.equals(eventStartDate)) {
                    g.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 11)); // 기본 폰트 사용
                    FontMetrics fm = g.getFontMetrics();
                    String eventText1 = "8/1-8/15";
                    String eventText2 = "여름 맞이 세일 대전";
                    int textWidth1 = fm.stringWidth(eventText1);
                    int textWidth2 = fm.stringWidth(eventText2);
                    int x1 = (getWidth() - textWidth1) / 2;
                    int x2 = (getWidth() - textWidth2) / 2;
                    g.drawString(eventText1, x1, getHeight() - fm.getHeight() * 2);
                    g.drawString(eventText2, x2, getHeight() - fm.getHeight());
                }
            } else if (!currentDate.isBefore(julyEventStartDate) && !currentDate.isAfter(julyEventEndDate)) {
                g.setColor(new Color(255, 192, 203)); // 분홍색으로 변경
                g.fillRect(0, getHeight() / 2, getWidth(), getHeight() / 2); // 칸의 하단 절반만 색칠
                g.setColor(Color.BLACK);
                if (currentDate.equals(julyEventStartDate)) {
                    g.setFont(new Font("한컴 말랑말랑 Bold", Font.PLAIN, 11)); // 기본 폰트 사용
                    FontMetrics fm = g.getFontMetrics();
                    String eventText1 = "7/1-7/15";
                    String eventText2 = "명품 김치 세일 대전";
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
