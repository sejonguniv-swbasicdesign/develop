package model.characters;

public abstract class Character {

    public int hp;
    public int x, y;
    public boolean isFacingRight;
    private boolean isMoving; // 현재 움직임 여부 저장
    private Thread currentActionThread;

    public Character() {
        hp = 3;
        x = 0;
        y = 0;
        isFacingRight = true;
        isMoving = false;
    }

    public Character(int x, int y) {
        this.hp = 3;
        this.x = x;
        this.y = y;
        this.isFacingRight = true;
        this.isMoving = false;
    }

    public int getHp() {
        return this.hp;
    }

    protected void resetHp() {
        hp = 3;
    }

    public abstract void move(int deltaX, int deltaY);

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // 외부에서 움직임 중단 요청
    public void stopCurrentAction() {
        if (currentActionThread != null && currentActionThread.isAlive()) {
            currentActionThread.interrupt(); // 현재 진행 중인 스레드 중단
        }
        isMoving = false;
    }

    public void jumpLeft(int deltaX, int deltaY) {
        if (isMoving) return; // 이미 움직이는 중이면 새로운 점프 무시
        isMoving = true;

        int targetX = this.x + deltaX;
        int originalY = this.y;
        int peakY = this.y - deltaY; // 점프하는 동안 상승할 y의 최고 위치

        currentActionThread = new Thread(() -> {
            try {
                while (this.y > peakY && this.x > targetX && isMoving) {
                    move(-3, -3); // 왼쪽 위로 조금씩 이동하여 점프
                    Thread.sleep(10); // 10ms 대기
                }
                fallLeft(targetX, originalY); // 하강 동작
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 인터럽트 예외 처리
            }
        });

        currentActionThread.start();
    }

    public void jumpRight(int deltaX, int deltaY) {
        if (isMoving) return; // 이미 움직이는 중이면 새로운 점프 무시
        isMoving = true;

        int targetX = this.x + deltaX;
        int originalY = this.y;
        int peakY = this.y - deltaY; // 점프하는 동안 상승할 y의 최고 위치

        currentActionThread = new Thread(() -> {
            try {
                while (this.y > peakY && this.x < targetX && isMoving) {
                    move(3, -3); // 오른쪽 위로 조금씩 이동하여 점프
                    Thread.sleep(10); // 10ms 대기
                }
                fallRight(targetX, originalY); // 하강 동작
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // 인터럽트 예외 처리
            }
        });

        currentActionThread.start();
    }

    private void fallLeft(int targetX, int originalY) {
        new Thread(() -> {
            try {
                while (this.y < originalY && isMoving) {
                    move(-3, 3); // 왼쪽 아래로 이동
                    Thread.sleep(10); // 10ms 대기
                }
                synchronized (this) {
                    this.y = originalY;
                    this.x = targetX;
                    isMoving = false; // 움직임 상태 초기화
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }

    private void fallRight(int targetX, int originalY) {
        new Thread(() -> {
            try {
                while (this.y < originalY && isMoving) {
                    move(3, 3); // 오른쪽 아래로 이동
                    Thread.sleep(10); // 10ms 대기
                }
                synchronized (this) {
                    this.y = originalY;
                    this.x = targetX;
                    isMoving = false; // 움직임 상태 초기화
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}
