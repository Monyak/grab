package edu.sl.grabalyze.grabber.factory;

import edu.sl.grabalyze.dao.ArticleDAO;
import edu.sl.grabalyze.entity.Article;
import edu.sl.grabalyze.grabber.strategy.GazetaUaArticleStrategy;
import edu.sl.grabalyze.grabber.strategy.GazetaUaListStrategy;
import edu.sl.grabalyze.grabber.strategy.GrabberStrategy;

import java.util.*;

public class GazetaUaArticleFactory implements GrabberStrategyFactory {

    private ArticleDAO articleDAO;
    private int countPerWorker;
    private Date beforeDate;

    public GazetaUaArticleFactory(int countPerWorker, Date beforeDate) {
        this.countPerWorker = countPerWorker;
        this.beforeDate = beforeDate;
    }

    public void setArticleDAO(ArticleDAO articleDAO) {
        this.articleDAO = articleDAO;
    }

    public List<GrabberStrategy> createStrategies(int count) {
        System.out.println("Requesting for article urls");
        List<GrabberStrategy> result = new ArrayList<>(count);
        List<Article> articles = articleDAO.getNotProcessedArticles(count * countPerWorker, beforeDate);
        for (int i = 0; i < count; i++) {
            HashMap<Long, String> map = new HashMap<>(countPerWorker);
            for (int j = 0; j < countPerWorker && j + i * countPerWorker < articles.size(); j++) {
                Article a = articles.get(j + i * countPerWorker);
                map.put(a.getId(), a.getUrl());
            }
            GazetaUaArticleStrategy strategy = new GazetaUaArticleStrategy(map);
            strategy.setArticleDAO(articleDAO);
            result.add(strategy);
        }
        System.out.println("Got " + articles.size() + " articles for " + count + " workers. "
                + "Dates:" + articles.get(0).getDate() + " to " + articles.get(articles.size()-1).getDate());
        return result;
    }
}
