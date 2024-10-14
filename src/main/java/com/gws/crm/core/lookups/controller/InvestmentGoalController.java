package com.gws.crm.core.lookups.controller;

import com.gws.crm.core.lookups.dto.LookupDTO;
import com.gws.crm.core.lookups.entity.InvestmentGoal;
import com.gws.crm.core.lookups.service.impl.InvestmentGoalService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/lookups/investment-goal")
public class InvestmentGoalController extends BaseLookupController<InvestmentGoal, LookupDTO> {

    protected InvestmentGoalController(InvestmentGoalService investmentGoalService) {
        super(investmentGoalService);
    }
}
