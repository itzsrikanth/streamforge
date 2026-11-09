package com.itzsrikanth.streamforge.model;

import java.sql.SQLException;

public interface ITickHandler {
    void onTick(Tick tick);
}
