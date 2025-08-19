package com.gws.crm.core.employee.dto;

import com.gws.crm.core.lookups.entity.Channel;
import com.gws.crm.core.lookups.entity.CommunicateWay;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class NeededLookupsForLead {
    private List<CommunicateWay> communicateWays;
    private List<Channel> channels;
}
