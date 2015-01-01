package org.codelibs.robot.db.bsbhv.pmbean;

import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import org.codelibs.robot.db.allcommon.DBFluteConfig;
import org.codelibs.robot.db.exbhv.AccessResultBhv;
import org.dbflute.jdbc.FetchBean;
import org.dbflute.outsidesql.PmbCustodial;
import org.dbflute.outsidesql.typed.CursorHandlingPmb;
import org.dbflute.util.DfTypeUtil;

/**
 * The base class for typed parameter-bean of AccessResultListByUrlDiff. <br>
 * This is related to "<span style="color: #AD4747">selectAccessResultListByUrlDiff</span>" on AccessResultBhv.
 * @author DBFlute(AutoGenerator)
 */
public class BsAccessResultListByUrlDiffPmb implements
        CursorHandlingPmb<AccessResultBhv, Void>, FetchBean {

    // ===================================================================================
    //                                                                           Attribute
    //                                                                           =========
    /** The parameter of oldSessionId. */
    protected String _oldSessionId;

    /** The parameter of newSessionId. */
    protected String _newSessionId;

    /** The max size of safety result. */
    protected int _safetyMaxResultSize;

    /** The time-zone for filtering e.g. from-to. (NullAllowed: if null, default zone) */
    protected TimeZone _timeZone;

    // ===================================================================================
    //                                                                         Constructor
    //                                                                         ===========
    /**
     * Constructor for the typed parameter-bean of AccessResultListByUrlDiff. <br>
     * This is related to "<span style="color: #AD4747">selectAccessResultListByUrlDiff</span>" on AccessResultBhv.
     */
    public BsAccessResultListByUrlDiffPmb() {
    }

    // ===================================================================================
    //                                                                Typed Implementation
    //                                                                ====================
    /**
     * {@inheritDoc}
     */
    @Override
    public String getOutsideSqlPath() {
        return "selectAccessResultListByUrlDiff";
    }

    /**
     * Get the type of an entity for result. (implementation)
     * @return The type instance of an entity, cursor handling. (NotNull)
     */
    @Override
    public Class<Void> getEntityType() {
        return Void.class;
    }

    // ===================================================================================
    //                                                                       Safety Result
    //                                                                       =============
    /**
     * {@inheritDoc}
     */
    @Override
    public void checkSafetyResult(final int safetyMaxResultSize) {
        _safetyMaxResultSize = safetyMaxResultSize;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getSafetyMaxResultSize() {
        return _safetyMaxResultSize;
    }

    // ===================================================================================
    //                                                                       Assist Helper
    //                                                                       =============
    // -----------------------------------------------------
    //                                                String
    //                                                ------
    protected String filterStringParameter(final String value) {
        return isEmptyStringParameterAllowed() ? value
                : convertEmptyToNull(value);
    }

    protected boolean isEmptyStringParameterAllowed() {
        return DBFluteConfig.getInstance().isEmptyStringParameterAllowed();
    }

    protected String convertEmptyToNull(final String value) {
        return PmbCustodial.convertEmptyToNull(value);
    }

    // -----------------------------------------------------
    //                                                  Date
    //                                                  ----
    protected Date toUtilDate(final Object date) {
        return PmbCustodial.toUtilDate(date, _timeZone);
    }

    protected <DATE> DATE toLocalDate(final Date date,
            final Class<DATE> localType) {
        return PmbCustodial.toLocalDate(date, localType, chooseRealTimeZone());
    }

    protected TimeZone chooseRealTimeZone() {
        return PmbCustodial.chooseRealTimeZone(_timeZone);
    }

    /**
     * Set time-zone, basically for LocalDate conversion. <br>
     * Normally you don't need to set this, you can adjust other ways. <br>
     * (DBFlute system's time-zone is used as default)
     * @param timeZone The time-zone for filtering. (NullAllowed: if null, default zone)
     */
    public void zone(final TimeZone timeZone) {
        _timeZone = timeZone;
    }

    // -----------------------------------------------------
    //                                    by Option Handling
    //                                    ------------------
    // might be called by option handling
    protected <NUMBER extends Number> NUMBER toNumber(final Object obj,
            final Class<NUMBER> type) {
        return PmbCustodial.toNumber(obj, type);
    }

    protected Boolean toBoolean(final Object obj) {
        return PmbCustodial.toBoolean(obj);
    }

    @SuppressWarnings("unchecked")
    protected <ELEMENT> ArrayList<ELEMENT> newArrayList(
            final ELEMENT... elements) {
        return PmbCustodial.newArrayList(elements);
    }

    // ===================================================================================
    //                                                                      Basic Override
    //                                                                      ==============
    /**
     * @return The display string of all parameters. (NotNull)
     */
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(DfTypeUtil.toClassTitle(this)).append(":");
        sb.append(xbuildColumnString());
        return sb.toString();
    }

    protected String xbuildColumnString() {
        final String dm = ", ";
        final StringBuilder sb = new StringBuilder();
        sb.append(dm).append(_oldSessionId);
        sb.append(dm).append(_newSessionId);
        if (sb.length() > 0) {
            sb.delete(0, dm.length());
        }
        sb.insert(0, "{").append("}");
        return sb.toString();
    }

    // ===================================================================================
    //                                                                            Accessor
    //                                                                            ========
    /**
     * [get] oldSessionId <br>
     * @return The value of oldSessionId. (NullAllowed, NotEmptyString(when String): if empty string, returns null)
     */
    public String getOldSessionId() {
        return filterStringParameter(_oldSessionId);
    }

    /**
     * [set] oldSessionId <br>
     * @param oldSessionId The value of oldSessionId. (NullAllowed)
     */
    public void setOldSessionId(final String oldSessionId) {
        _oldSessionId = oldSessionId;
    }

    /**
     * [get] newSessionId <br>
     * @return The value of newSessionId. (NullAllowed, NotEmptyString(when String): if empty string, returns null)
     */
    public String getNewSessionId() {
        return filterStringParameter(_newSessionId);
    }

    /**
     * [set] newSessionId <br>
     * @param newSessionId The value of newSessionId. (NullAllowed)
     */
    public void setNewSessionId(final String newSessionId) {
        _newSessionId = newSessionId;
    }
}