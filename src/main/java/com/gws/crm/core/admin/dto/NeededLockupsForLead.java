package com.gws.crm.core.admin.dto;

import com.gws.crm.core.lockups.entity.Channel;
import com.gws.crm.core.lockups.entity.CommunicateWay;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NeededLockupsForLead {
    private List<CommunicateWay> communicateWays;
    private List<Channel> channels;
}
