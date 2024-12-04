package controller.stage2;

import model.monsters.TigerMonster;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.*;

public class MonsterSpawner {

    private List<TigerMonster> tigers;
    private List<Boolean> isTigerFalling;
    private ScheduledExecutorService monsterSpawnExecutor;
    private int panelWidth, panelHeight;
    private Container container; // 실제 타입으로 변경 필요
    private JPanel panel;

    public MonsterSpawner() {
    }

    public void setContainer(Container container) {
        this.container = container;
    }
    public void setPanel(JPanel panel) {
        this.panel = panel;
    }

    public void setMonster() {
        tigers = new ArrayList<>();
        isTigerFalling = new ArrayList<>();

        // 첫 번째 몬스터 생성
        TigerMonster tigerMonster = new TigerMonster(50, 75);
        tigerMonster.setMonster(panelWidth, panelHeight, panel);
        tigers.add(tigerMonster);
        isTigerFalling.add(false);
        container.repaint();

        // ScheduledExecutorService로 몬스터 스폰 작업 설정
        monsterSpawnExecutor = Executors.newScheduledThreadPool(1);
        monsterSpawnExecutor.scheduleAtFixedRate(() -> {
            SwingUtilities.invokeLater(() -> {
                TigerMonster tigerMonster2 = new TigerMonster(50, 75);
                tigerMonster2.setMonster(panelWidth, panelHeight, panel);
                tigers.add(tigerMonster2);
                isTigerFalling.add(false);
                container.repaint();
            });
        }, 20, 20, TimeUnit.SECONDS); // 초기 지연 20초, 이후 20초 간격으로 실행
    }

    public void stopMonsterSpawn() {
        if (monsterSpawnExecutor != null && !monsterSpawnExecutor.isShutdown()) {
            monsterSpawnExecutor.shutdown(); // 스레드 풀 종료
            try {
                if (!monsterSpawnExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                    monsterSpawnExecutor.shutdownNow(); // 강제 종료
                }
            } catch (InterruptedException e) {
                monsterSpawnExecutor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
}

