package com.hyf.cache.test.service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hyf.cache.annotation.EpCacheEvict;
import com.hyf.cache.test.entity.FrameUser;
import com.hyf.cache.test.entity.PageData;

public interface IUserService
{
    String encryption(String pwd, String loginId);

    String encryption(String pwd, String userGuid, Date updatePwd);

    boolean checkPassword(String loginId, String password);

    boolean checkPassword(String loginId, String password, String loadType);

    void updateUserLoginTime(Date upDateTime, String userGuid);

    List<FrameUser> listUserByOuGuid(String ouguid, String roleGuid, String baseOuGuid, String userCondition,
                                     boolean fetchOnlineUser, boolean partTimeOu, boolean onlyCurrentBaseOu, int level);

    List<FrameUser> listUserByOuGuid(String ouguid, String roleGuid, String baseOuGuid, String userCondition,
            boolean fetchOnlineUser, boolean partTimeOu, boolean onlyCurrentBaseOu, int level, Integer secretLevel,
            String filterRule);

    int getUserCountByOuGuid(String ouguid, String roleGuid, String baseOuGuid, String userCondition,
            boolean fetchOnlineUser, boolean partTimeOu, boolean onlyCurrentBaseOu, int level);

    List<FrameUserSecondOU> listFrameUserSecondOU(String userGuid);

    int getFrameOnlineUserCount();

    List<String> listAllOnlineUserGuid();

    List<String> listAllOnlineLoginId();

    boolean isOnline(String userGuid);

    List<FrameSecretaryInfo> listSecretaryInfoByLeaderGuid(String leaderGuid);

    List<FrameSecretaryInfo> listSecretaryInfoBySecretaryGuid(String secretaryGuid);

    PageData<ViewFrameUser> paginatorUserViewByOuGuid(String ouguid, String displayname, String loginid, String ouname,
                                                      String txtLink, String baseOuGuid, int firstResult, int maxResults, String sortKey, String sortBy,
                                                      boolean checkEnable, boolean direct);

    PageData<ViewFrameUser> paginatorUserViewByOuGuid(String ouguid, String displayname, String loginid, String ouname,
            String txtLink, String baseOuGuid, int firstResult, int maxResults, String sortKey, String sortBy,
            boolean checkEnable, boolean direct, boolean onlyCurrentBaseOu);

    PageData<ViewFrameUser> paginatorUserViewByOuGuid(String ouguid, String displayname, String loginid, String ouname,
            String txtLink, String baseOuGuid, int firstResult, int maxResults, String sortKey, String sortBy,
            boolean checkEnable, boolean direct, boolean onlyCurrentBaseOu, Integer enable);

    List<String> listUserRelation(String userGuid);

    FrameUser getUserByUserField(String filedName, String fieldValue);

    String getUserNameByUserGuid(String userGuid);

    FrameUserExtendInfo getUserExtendInfoByUserGuid(String userGuid);

    FrameUserExtendInfo getUserExtendInfoWithPicContent(String userGuid);

    List<FrameUser> getUserListByOuGuidWithNotEnabled(String ouguid, String roleGuid, String baseOuGuid,
            String userCondition, boolean fetchOnlineUser, boolean partTimeOu, boolean onlyCurrentBaseOu, int level);

    List<FrameUser> getUserListByOuGuidWithNotEnabled(String ouguid, String roleGuid, String baseOuGuid,
            String userCondition, boolean fetchOnlineUser, boolean partTimeOu, boolean onlyCurrentBaseOu, int level,
            Integer secretLevel, String filterRule);

    String[] getRelationOuNameAndGuid(String userGuid, boolean fetchGuidOnly);

    PageData<ViewFrameUser> paginatorUserViewByOuGuid(String ouguid, String displayname, String loginid, String ouname,
            String txtLink, String baseOuGuid, int firstResult, int maxResults, String sortKey, String sortBy,
            boolean direct);

    @EpCacheEvict
    void addUserPicContent(AttachStorage storage, String mobile);

    byte[] getUserPicContent(String userGuid);

    FrameAttachStorage getUserPicContentNative(String userGuid);

    void deleteUserPicContent(String userGuid);

    void addUserSignImage(AttachStorage storage, String userGuid);

    FrameAttachStorage getUserSignImage(String userGuid);

    void deleteUserSignImage(String userGuid);

    PageData<ViewFrameUser> getUserViewListPageDataByNameOrTxt(String ouguid, String displaynameOrtxt, String loginid,
            String ouname, String baseOuGuid, int firstResult, int maxResults, String sortKey, String sortBy);

    PageData<ViewFrameUser> getUserViewListByNameOrTxt(String displaynameOrtxt);

    Integer getSecretLevelByUserGuid(String userGuid);

    void createViewFrameUser(Boolean frameMj);

    String getUserMobile(String userGuid, String mobile, String sessionUserGuid, boolean isAdmin);

    String getUserMobile(String userGuid, String mobile, String sessionUserGuid, boolean isAdmin, String configName);

    Map<String, String> sortUser(List<String> guids, String direction);

    Set<String> listSecondOUGuidByUserGuid(String userGuid);

    String savePwdEditStatus(String loginId);

    String savePwdEditStatus(String mobile, String platform, String email);

    void clearPwdEditStatus(String loginId);

    List<FrameUser> getUserListWithRoleRelationByUserName(String userName);

    Set<String> getUserGuidsByBaseOuguid(String baseOuGuid);

    List<String> getOuTitleInfoList(String userguid, String ouguid, List<String> roleList, String objectguid);

    class FrameSecretaryInfo
    {
    }

    class FrameAttachStorage
    {
    }

    class AttachStorage
    {
    }

    class ViewFrameUser
    {
    }

    class FrameUserSecondOU
    {
    }

    class FrameUserExtendInfo
    {
    }
}
