package view;

import model.CostumeStatistic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class CostumeStatisticSearchState {
    private final Date startDate;
    private final Date endDate;
    private final List<CostumeStatistic> costumeStatistics;

    public CostumeStatisticSearchState(Date startDate, Date endDate, List<CostumeStatistic> costumeStatistics) {
        this.startDate = copyDate(startDate);
        this.endDate = copyDate(endDate);
        this.costumeStatistics = new ArrayList<>(costumeStatistics == null ? Collections.emptyList() : costumeStatistics);
    }

    public Date getStartDate() {
        return copyDate(startDate);
    }

    public Date getEndDate() {
        return copyDate(endDate);
    }

    public List<CostumeStatistic> getCostumeStatistics() {
        return Collections.unmodifiableList(costumeStatistics);
    }

    private Date copyDate(Date date) {
        return date == null ? null : new Date(date.getTime());
    }
}
