package tech.theraven.cloudrender.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import tech.theraven.cloudrender.domain.AnalysisInfo;
import tech.theraven.cloudrender.domain.Job;
import tech.theraven.cloudrender.domain.JobSpecs;
import tech.theraven.cloudrender.domain.WorkUnit;
import tech.theraven.cloudrender.domain.enums.JobStatus;
import tech.theraven.cloudrender.repository.WorkUnitRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
class WorkUnitServiceTest {

    @Mock
    WorkUnitRepository workUnitRepository;

    WorkUnitService workUnitService = new WorkUnitService(workUnitRepository, 10L);


    @Test
    void brokeIntoUnitsTest() {
        var job = Job.builder()
                .id(1L)
                .fileUrl("hello.file")
                .analysis(new AnalysisInfo(104L, 10L))
                .specs(new JobSpecs("resolution"))
                .status(JobStatus.NEW)
                .build();

        List<WorkUnit> units = workUnitService.brokeIntoWorkUnits(job).getData();
        units.forEach(u -> System.out.println(u + "\n"));


        assertEquals(10, units.size());

        assertEquals(1, units.get(0).getStartFrame());
        assertEquals(11, units.get(1).getStartFrame());
        assertEquals(21, units.get(2).getStartFrame());
        assertEquals(31, units.get(3).getStartFrame());
        assertEquals(41, units.get(4).getStartFrame());
        assertEquals(51, units.get(5).getStartFrame());
        assertEquals(61, units.get(6).getStartFrame());
        assertEquals(72, units.get(7).getStartFrame());
        assertEquals(83, units.get(8).getStartFrame());
        assertEquals(94, units.get(9).getStartFrame());

        assertEquals(10, units.get(0).getEndFrame());
        assertEquals(20, units.get(1).getEndFrame());
        assertEquals(30, units.get(2).getEndFrame());
        assertEquals(40, units.get(3).getEndFrame());
        assertEquals(50, units.get(4).getEndFrame());
        assertEquals(60, units.get(5).getEndFrame());
        assertEquals(71, units.get(6).getEndFrame());
        assertEquals(82, units.get(7).getEndFrame());
        assertEquals(93, units.get(8).getEndFrame());
        assertEquals(104, units.get(9).getEndFrame());


    }

}