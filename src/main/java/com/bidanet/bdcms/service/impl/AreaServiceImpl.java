package com.bidanet.bdcms.service.impl;

import com.bidanet.bdcms.dao.AreaDao;
import com.bidanet.bdcms.dao.Dao;
import com.bidanet.bdcms.entity.Area;
import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;
import org.hibernate.criterion.MatchMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
*
*/
@Service("areaService")
public class AreaServiceImpl extends BaseServiceImpl<Area> implements com.bidanet.bdcms.service.AreaService {
    @Autowired
    private AreaDao areaDao;
    @Override
    protected Dao getDao() {
        return areaDao;
    }

    @Override
    public List<Area> getRoot(){
        Area area = new Area();
        area.setParentId(0L);
        List list = getDao().findByExample(area);
        return list;
    }

    @Override
    public void addAreaT(Area area) {
        checkString(area.getName(),"请输入名称！");
        checkString(area.getCode(),"请输入代号！");
        Area checkArea = new Area();
        checkArea.setCode(area.getCode());
        List<Area> checkAreas = areaDao.findByExample(checkArea, MatchMode.EXACT);
        if(checkAreas!=null&&checkAreas.size()>0) {
            errorMsg("该城市代号已有，请更换！");
        }
        area.setPinyin(getFullSpell(area.getName()));
        areaDao.save(area);
    }

    @Override
    public void updateAreaT(Area area) {
        checkString(area.getName(),"请输入名称！");
        Area upArea = areaDao.get(area.getId());
        checkNull(upArea,"没有找到这个地区！");
        upArea.setName(area.getName());
        upArea.setPinyin(getFullSpell(area.getName()));
        areaDao.update(upArea);
    }

    @Override
    public void deleteAreaT(Long id) {
        Area area = areaDao.get(id);
        checkNull(area,"没有找到这个地区！");
        if(area.getParentId()==0){
            Area parArea = new Area();
            parArea.setParentId(id);
            long l = areaDao.countByExample(parArea);
            if(l>0) {
                errorMsg("该省份有下属城市，暂不能被删除！");
            }
            areaDao.delete(id);
        }else{
            areaDao.delete(id);
        }
    }

    /**
     * 汉字转拼音
     * @param name
     * @return
     */
    /**
     * 获取汉字串拼音，英文字符不变
     * @param chinese 汉字串
     * @return 汉语拼音
     */
    public static String getFullSpell(String chinese) {
        StringBuffer pybf = new StringBuffer();
        char[] arr = chinese.toCharArray();
        HanyuPinyinOutputFormat defaultFormat = new HanyuPinyinOutputFormat();
        defaultFormat.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        defaultFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 128) {
                try {
                    pybf.append(PinyinHelper.toHanyuPinyinStringArray(arr[i], defaultFormat)[0]);
                } catch (BadHanyuPinyinOutputFormatCombination e) {
                    e.printStackTrace();
                }
            } else {
                pybf.append(arr[i]);
            }
        }
        return pybf.toString();
    }

    /**
     * 根据省id查已开通的城市
     * @param pid
     * @return
     */
    @Override
    public List<Area> getOpenCityByPid(Long pid) {
        if(pid==null || pid==0L){
            return new ArrayList<Area>();
        }
        Area province = new Area();
        province.setParentId(pid);
        return areaDao.findByExample(province);
    }

    @Override
    public List<Area> getArea(String name) {
        List<Area> list=new ArrayList<>();
        List<Area> areaList=areaDao.getList(name);
        return areaList;
    }

    @Override
    public Long getCityId(String name) {
       Area area=areaDao.getCityId(name);
        return area.getId();
    }

    @Override
    public List<Area> getAllNotParentCity() {
        List<Area> list = areaDao.getAllNotParentCity();
        return list;
    }

}
