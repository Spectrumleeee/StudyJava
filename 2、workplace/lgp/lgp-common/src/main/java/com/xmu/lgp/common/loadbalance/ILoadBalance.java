/**
 * Copyright (c) 2015, TP-Link Co.,Ltd.
 * Author: lukairui <lukairui@tp-link.net>
 * Created: 2015-09-28
 */

package com.xmu.lgp.common.loadbalance;

import java.util.List;

/**
 * Load balance
 */
public interface ILoadBalance {
    public String select(List<ServerAddressNode> serverNodes);

    /**
     * RANDOM, RANDOM_ROBIN, HASH, WEIGHTED are Simple strategies
     * 
     * LEAST_BUSY, TOTAL_LEAST_BUSY, FASTEST_RESP are dynamic strategies,
     * based on monitored control system
     * 
     * Marked, base on the connection num and response time,
     * the fewer connection and faster response, the mark is higher,
     * and the higher mark server will be given more chance to serve.
     * 
     * Anticipate, based on MARKED strategy, hold the history marks
     * of all server, if the mark is increasing, the server will be
     * given more chance to serve.
     */
    public enum Strategy {
        RANDOM, RANDOM_ROBIN, HASH, WEIGHTED, LEAST_BUSY, TOTAL_LEAST_BUSY,
        FASTEST_RESP, MARKED, ANTICIPATE;
    }

    public static final String NODE_STATE = "state";

    public enum ServerState {
        ONLINE("online"), OFFLINE("offline"), STOPPING("stopping");

        private final String state;

        private ServerState(String state) {
            this.state = state;
        }

        public String getState() {
            return this.state;
        }

        public static ServerState getServerState(String state) {
            for (ServerState serverState : ServerState.values()) {
                if (serverState.getState().equals(state)) {
                    return serverState;
                }
            }
            return null;
        }
    }
}
