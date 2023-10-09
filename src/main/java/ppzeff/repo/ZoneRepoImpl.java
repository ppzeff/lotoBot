package ppzeff.repo;//package com.ppzeff.danloto.repo;
//
//import com.ppzeff.danloto.entity.Zone;
//import org.springframework.stereotype.Repository;
//
//import java.util.ArrayList;
//import java.util.List;
//
//@Repository
//public class ZoneRepoImpl implements ZoneRepo {
//    private final List<Zone> list = new ArrayList<>();
//
//
//    @Override
//    public List<Zone> getAllZone() {
//        return list;
//    }
//
//    @Override
//    public Zone findZoneById(String myId) {
//        return list.stream().filter(el -> el.getMyId().equals(myId)).findFirst().get();
//    }
//
//    public ZoneRepoImpl() {
//        list.add(new Zone("zoneId1", "АХУ"));
//        list.add(new Zone("zoneId2", "ССиМ"));
//        list.add(new Zone("zoneId3", "Територия"));
//        list.add(new Zone("zoneId4", "Фасовка"));
//    }
//}
