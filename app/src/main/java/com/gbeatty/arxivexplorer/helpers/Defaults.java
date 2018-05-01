package com.gbeatty.arxivexplorer.helpers;

import com.gbeatty.arxivexplorer.network.ArxivAPI;

public class Defaults {
    public static final String SORT_ORDER = ArxivAPI.SORT_ORDER_DESCENDING;
    public static final String SORT_BY = ArxivAPI.SORT_BY_SUBMITTED_DATE;
    public static final String MAX_RESULTS = "10";
    public static final boolean RENDER_LATEX = false;
    public static final boolean DARK_MODE = false;
    public static final boolean SHOW_ABSTRACT = true;
}
