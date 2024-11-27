package com.aclib.aclib_deploy.ScheduleService;

import com.aclib.aclib_deploy.Service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class LoanScheduleService {

    @Autowired
    private final LoanService loanService;

    public LoanScheduleService(LoanService loanService) {
        this.loanService = loanService;
    }

//    @Scheduled(cron = "0 0 0 * * ?") // 0st sec 0nd minute 0rd hour
        @Scheduled(fixedRate = 60000) // Runs every 60 seconds
        public void runOverdueLoanCheck() {
            loanService.checkOverDueDateLoans();
        System.out.println("Scheduled overdue loan check executed at " + LocalDateTime.now());
    }
}
