package edu.sl.grabalyze.grabber.factory;

import java.util.ArrayList;
import java.util.List;

import edu.sl.grabalyze.execution.Callback;
import edu.sl.grabalyze.execution.RunnableFactory;
import edu.sl.grabalyze.grabber.GrabberImpl;
import edu.sl.grabalyze.grabber.strategy.GrabberStrategy;

public class GrabberFactory implements RunnableFactory {

    GrabberStrategyFactory strategyFactory;

    public void setStrategyFactory(GrabberStrategyFactory strategyFactory) {
        this.strategyFactory = strategyFactory;
    }

    @Override
    public List<Runnable> create(int count) {
        List<GrabberStrategy> strategies = strategyFactory.createStrategies(count);
        List<Runnable> result = new ArrayList<>(strategies.size());
        int id = 0;
        for (GrabberStrategy strategy : strategies) {
            GrabberImpl g = new GrabberImpl();
            g.setId(++id);
            g.setStrategy(strategy);
            result.add(g);
        }
        return result;
    }

    @Override
    public Callback<List<Runnable>> createCallback() {
        return new Callback<List<Runnable>>() {
            @Override
            public void onSuccess(List<Runnable> result) {}
        };
    }

}
