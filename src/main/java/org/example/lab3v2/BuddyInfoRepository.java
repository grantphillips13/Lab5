package org.example.lab3v2;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface BuddyInfoRepository extends CrudRepository<BuddyInfo, Long> {
    List<BuddyInfo> findByName(String name);

    BuddyInfo findByAddress(String address);
}