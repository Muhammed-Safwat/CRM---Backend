package com.gws.crm.core.lockups.service;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.lockups.entity.InvestmentGoal;

import java.util.List;

public interface InvestmentGoalService {
    List<InvestmentGoal> getAllInvestmentGoal(Transition transition);
}
