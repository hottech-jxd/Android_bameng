package com.bameng.model;

import java.util.List;

/**
 * 本地维护城市地址数据信息
 */
public class LocalAddressModel extends BaseModel {


    private List<ProvinceInner> list;

    public List<ProvinceInner> getList() {
        return list;
    }

    public void setList(List<ProvinceInner> list) {
        this.list = list;
    }

    public class ProvinceInner
    {
        private String name;
        private List<CityInner> city;

        public List<CityInner> getCity() {
            return city;
        }

        public void setCity(List<CityInner> city) {
            this.city = city;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
    public class CityInner
    {
        private String name;
        private List<AreaInner> area;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public List<AreaInner> getArea() {
            return area;
        }

        public void setArea(List<AreaInner> area) {
            this.area = area;
        }
    }
    public class AreaInner
    {
        private long id;
        private String name;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
