package tech.theraven.cloudrender.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import tech.theraven.cloudrender.domain.AnalysisInfo;
import tech.theraven.cloudrender.domain.Job;
import tech.theraven.cloudrender.domain.JobSpecs;
import tech.theraven.cloudrender.domain.WorkUnit;
import tech.theraven.cloudrender.domain.enums.JobStatus;
import tech.theraven.cloudrender.repository.WorkUnitRepository;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@FieldDefaults(level = AccessLevel.PRIVATE)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class WorkUnitServiceTest {

    @Mock
    WorkUnitRepository workUnitRepository;
    @Mock
    GcpStorageService gcpStorageService;


    WorkUnitService workUnitService = new WorkUnitService(gcpStorageService, workUnitRepository);

    @BeforeAll
    public void setUp() {
        ReflectionTestUtils.setField(workUnitService, "framesPerUnit", 10);
    }

    @Test
    void brokeIntoUnitsTest() {
        var job = Job.builder()
                .id(1L)
                .fileUrl("hello.file")
                .workUnits(Collections.emptyList())
                .analysis(new AnalysisInfo(104L, 10L))
                .specs(new JobSpecs("resolution"))
                .status(JobStatus.NEW)
                .build();

        List<WorkUnit> units = workUnitService.brokeIntoWorkUnits(job).getData();
        units.forEach(u -> System.out.println(u + "\n"));


        assertEquals(10, units.size());

        assertEquals(0, units.get(0).getStartFrame());
        assertEquals(10, units.get(1).getStartFrame());
        assertEquals(20, units.get(2).getStartFrame());
        assertEquals(30, units.get(3).getStartFrame());
        assertEquals(40, units.get(4).getStartFrame());
        assertEquals(50, units.get(5).getStartFrame());
        assertEquals(60, units.get(6).getStartFrame());
        assertEquals(71, units.get(7).getStartFrame());
        assertEquals(82, units.get(8).getStartFrame());
        assertEquals(93, units.get(9).getStartFrame());

        assertEquals(9, units.get(0).getEndFrame());
        assertEquals(19, units.get(1).getEndFrame());
        assertEquals(29, units.get(2).getEndFrame());
        assertEquals(39, units.get(3).getEndFrame());
        assertEquals(49, units.get(4).getEndFrame());
        assertEquals(59, units.get(5).getEndFrame());
        assertEquals(70, units.get(6).getEndFrame());
        assertEquals(81, units.get(7).getEndFrame());
        assertEquals(92, units.get(8).getEndFrame());
        assertEquals(103, units.get(9).getEndFrame());


    }

}