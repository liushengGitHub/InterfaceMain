package liusheng.main.app.bilibili.processor.animal;

import com.google.gson.Gson;
import liusheng.main.app.bilibili.entity.*;
import liusheng.main.app.bilibili.entity.animal.AnimalPagesBean;
import liusheng.main.app.bilibili.util.ConnectionUtils;
import liusheng.main.app.bilibili.util.StringUtils;
import liusheng.main.listener.ProcessListener;
import liusheng.main.process.AbstractLinkedListableProcessor;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class AnimalPagesBeanToVideoBean extends AbstractLinkedListableProcessor<AnimalPagesBean, AnimalPagesBeanToVideoBean> {
    private static final String API = "https://api.bilibili.com/pgc/player/web/playurl?otype=json";
    private static final String EP_ID = "ep_id";
    private static final String QN = "qn";

    private final String downloadDir;
    private final int qn;

    public AnimalPagesBeanToVideoBean(String downloadDir, int qn) {

        this.downloadDir = Objects.requireNonNull(downloadDir);
        this.qn = qn;
        addListener(new ProcessListener() {
            @Override
            public void endDo(Object source, Object target) {
                AnimalPagesBeanToVideoBean animalPagesBeanToVideoBean = (AnimalPagesBeanToVideoBean) source;
            }
        });
    }

    public AnimalPagesBeanToVideoBean(String downloadDir) {
        this(downloadDir, 80);
    }

    @Override
    protected void doProcess(AnimalPagesBean animalPagesBean, List<Object> returnData) throws Throwable {
        // 获取动画片的名字
        String title = animalPagesBean.getMediaInfo().getTitle();

        if (title == null) {
            title = UUID.randomUUID().toString();
        }
        // 去除特殊字符
        Path path = Paths.get(downloadDir, StringUtils.fileNameHandle(title));
        if (!Files.exists(path)) {

            Files.createDirectories(path);

        }
        if (!animalPagesBean.isSingle()) {
            Optional.ofNullable(animalPagesBean.getEpList())
                    .ifPresent(epListBeans -> {
                        epListBeans.forEach(epListBean -> {
                            notNullAnd(animalPagesBean, returnData, path, epListBean);
                        });
                    });

        } else {
            Optional.ofNullable(animalPagesBean.getEpInfo())
                    .ifPresent(epInfoBean -> {
                        notNullAnd(animalPagesBean, returnData, path, epInfoBean);
                    });
        }
        // 设置大小
        returnData.stream().forEach(o->{
            AbstractVideoBean abstractVideoBean  = (AbstractVideoBean) o;
            abstractVideoBean.setPageSize(returnData.size());
        });
    }

    private void notNullAnd(AnimalPagesBean animalPagesBean, List<Object> returnData, Path path, AnimalPagesBean.EpInfoBean epInfoBean) {
        AbstractVideoBean abstractVideoBean = createAndAdd(animalPagesBean, epInfoBean);
        if (abstractVideoBean != null) {
            abstractVideoBean.setDirFile(path.toFile());
            returnData.add(abstractVideoBean);
        }
    }

    private AbstractVideoBean createAndAdd(AnimalPagesBean animalPagesBean, AnimalPagesBean.EpInfoBean epInfoBean) {
        try {
            int index = epInfoBean.getI();
            String longTitle = StringUtils.fileNameHandle(epInfoBean.getLongTitle());
            int id = epInfoBean.getId();
            if (epInfoBean.getBadge().contains("会员")) return null;

            String url = API + "&" + EP_ID + "=" + id + "&" + QN + "=" + qn;
            String json = ConnectionUtils.getConnection(url).execute().body();
            json = json.replace("\"result\"", "\"data\"");
            Gson gson = new Gson();
            AbstractVideoBean abstractVideoBean;

            if (json.contains("\"dash\"")) {
                abstractVideoBean = gson.fromJson(json, NewVideoBean.class);
            } else {
                abstractVideoBean = gson.fromJson(json, OldVideoBean.class);
            }
            String fileName = (index + 1) + "_" + longTitle;

            abstractVideoBean.setName(fileName);
            abstractVideoBean.setUrl(animalPagesBean.getUrl());
            return abstractVideoBean;

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
