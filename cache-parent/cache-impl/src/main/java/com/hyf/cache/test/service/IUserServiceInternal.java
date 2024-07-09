package com.hyf.cache.test.service;

import com.hyf.cache.test.entity.FrameUser;
import com.hyf.cache.test.entity.PageData;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IUserServiceInternal extends IUserService
{

    void addFrameUser(FrameUser user, FrameUserExtendInfo frameUserExtendInfo);

    String deleteUserByUserGuid(String guid);

    void updateUserOrderNumber(String userGuid, Integer orderNumber, String currentSecondOu);

    void updateFrameUser(FrameUser user, FrameUserExtendInfo frameUserExtendInfo);

    void updateFrameUserWithOutPic(FrameUser user, FrameUserExtendInfo frameUserExtendInfo);

    void updateUserPassWord(String userGuid, String password);

    void updateUserMobile(String userGuid, String mobile);

    void updateUserPassWord(String userGuid, String password, String passwordType);

    void updateUserPassWord(String userGuid, String password, String passwordType, Date updatePwd);

    void updateGesturePassWord(String userGuid, String gesturePassword);

    void onceRePassword();

    Integer countRePassword();

    void finishRePassword();

    void cancelUpdate();

    void updateUserPassWordEncrypt(String userGuid, String password);

    boolean isExistLoginID(String loginID, String userGuid);

    boolean isExistMobile(String mobile, String userGuid);

    boolean isExistEmail(String email, String userGuid);

    List<FrameUser> getUserListByUserguids(List<String> userGuids);

    List<FrameUser> getUserList(Map<String, Object> conditionMap, int firstResult, int maxResults, String sortKey,
            String sortBy);

    void addUserRelation(FrameUserSecondOU userRelation);

    void deleteUserRelation(String userGuid, List<String> ouList);

    int getUserRelationCount(String userGuid);

    String checkUsbIsExist(String usb);

    FrameUserExtendInfo getUserExtendInfoByIdentityCard(String identityCardNum);

    void addSecretaryInfo(FrameSecretaryInfo secretaryInfo);

    void delSecretaryInfoByLeaderGuid(String leaderGuid);

    void delSecretaryInfoByLeaderGuid(String leaderGuid, String ouGuid);

    void delSecretaryInfoByUserGuid(String userGuid);

    List<FrameSecretaryInfo> listSecretaryInfoByLeaderGuid(String leaderGuid, String ouGuid);

    List<FrameSecretaryInfo> getAllSecretaryInfo();

    List<FrameUserSecondOU> getFrameUserSecondOUListByUserGuid(String userGuid, int firstResult, int maxResults,
            String sortKey, String sortBy);

    void updateFrameUserSecondOU(FrameUserSecondOU frameUserSecondOU);

    List<FrameUserSecondOU> getAllSecondOu();

    void deleteSecondOu(FrameUserSecondOU frameUserSecondOU);

    void deleteSecondOuByOuGuid(String ouguid);

    FrameUserSecondOU getUserRelation(String userGuid, String ouGuid);

    String updateUserRelation(String ouguids, FrameUser user);

    List<FrameUserExtendInfo> listAllFrameUserExtendInfo();

    List<FrameUser> listAllUser();

    void updateFrameUserStepSize(List<FrameUser> frameUserList, int stepSize);

    boolean isEnableSheMi();

    List<FrameUser> listUserByCarNum(String carNum);

    List<FrameAccountRelation> listAllAccountRelation();

    List<FrameAccountRelation> getAccountRelation(String userGuid);

    PageData<FrameAccountRelation> getAccountRelation(String userGuid, int first, int pageSize, String sortField,
                                                      String sortOrder);

    void deleteAccountRelation(String rowguid);

    void deleteAccountRelationByUserGuid(String userGuid);

    void updateAccountRelation(FrameAccountRelation accountRelation);

    void addAccountRelation(FrameAccountRelation accountRelation);

    FrameAccountRelation getAccountRelationByRelativeUserGuid(String relativeUserGuid);

    String deleteUserByFrameUserList(List<FrameUser> FrameUserDeleteList);

    List<FrameUserSecondOU> getUserRelationByOuGuid(String ouGuid);

    FrameAccountRelation getAccountRelationByRowguid(String rowguid);

    String deleteUserByUserGuid(String guid, String dimension);

    void updateUserOrderNumber(String userGuid, Integer orderNumber, String currentSecondOu, String dimension);

    List<FrameUser> getUserListByUserguids(List<String> userGuids, String dimension);

    List<FrameUser> getUserList(Map<String, Object> conditionMap, int firstResult, int maxResults, String sortKey,
            String sortBy, String dimension);

    int getUserRelationCount(String userGuid, String dimension);

    List<FrameUserSecondOU> getFrameUserSecondOUListByUserGuid(String userGuid, int firstResult, int maxResults,
            String sortKey, String sortBy, String dimension);

    void updateFrameUserStepSize(List<FrameUser> frameUserList, int stepSize, String dimension);

    List<FrameUser> listSoaAllUser(String dimension);

    FrameUser getSoaUserByUserGuid(String userGuid, String dimension);

    String updateFrameUserStepSize(String ouguid, String roleGuid, String baseOuGuid, String userCondition,
            Boolean fetchOnlineUser, Boolean partTimeOu, Boolean onlyCurrentBaseOu, Integer level, Integer stepSize);

    boolean isExistIdentityCardNum(String identityCardNum, String userGuid);

    String getColorByUserName(String userGuid);

    Map<String, String> getPictureParams(String userGuid, boolean isNativePic);

    List<Map<String, String>> getUserCenterOuInfo(String userguid, String ouguid, List<String> roleList);

    List<FrameUser> listAllUserByKeyWord(String keyword);

    Map<String, String> getPictureColorParams(String userGuid);

    class FrameAccountRelation
    {
    }
}
