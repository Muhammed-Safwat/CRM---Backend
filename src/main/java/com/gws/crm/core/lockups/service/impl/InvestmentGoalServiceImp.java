package com.gws.crm.core.lockups.service.impl;

import com.gws.crm.common.entities.Transition;
import com.gws.crm.core.admin.repository.AdminRepository;
import com.gws.crm.core.lockups.entity.InvestmentGoal;
import com.gws.crm.core.lockups.repository.InvestmentGoalRepository;
import com.gws.crm.core.lockups.service.InvestmentGoalService;
import jakarta.transaction.Transaction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InvestmentGoalServiceImp implements InvestmentGoalService {
    private final InvestmentGoalRepository investmentGoalRepository;
    private final AdminRepository adminRepository;
    @Override
    public List<InvestmentGoal> getAllInvestmentGoal(Transition transition) {
        return investmentGoalRepository.findAllByAdminId(transition.getUserId());
    }
}
