package ScheduleService;

import com.aclib.aclib_deploy.ScheduleService.LoanScheduleService;
import com.aclib.aclib_deploy.Service.LoanService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

public class LoanScheduleServiceTest {
    private LoanScheduleService loanScheduleService;

    private LoanService loanService;

    @BeforeEach
    public void setUp() {
        loanService = mock(LoanService.class);
        loanScheduleService = new LoanScheduleService(loanService);
    }

    @Test
    public void testRunOverdueLoan() {
        doNothing().when(loanService);
        loanScheduleService.runOverdueLoanCheck();
    }
}
