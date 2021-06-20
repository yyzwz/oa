package cn.exrick.xboot.modules.crm.entity;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class JiLianCity {

    private String value;
    private String label;
    private List<JiLianCity> children = new ArrayList<JiLianCity>();
}
