package io.github.pactstart.simple.web.framework.common.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;

@Getter
@Setter
public class PageForm {

    @Min(value = 1, message = "当前页码不合法")
    private int pageNum = 1;

    @Min(value = 1, message = "每页展示数量不合法")
    private int pageSize = 10;

}
