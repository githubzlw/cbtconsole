package com.cbt.warehouse.service;

import com.cbt.warehouse.dao.OrderSnapshotDao;
import com.cbt.warehouse.pojo.OrderSnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class OrderSnapshotServiceImpl implements OrderSnapshotService {

	@Autowired
	private OrderSnapshotDao orderSnapshotDao;

	@Override
	public boolean syncOfflineToOnline() {
		return orderSnapshotDao.syncOfflineToOnline();
	}

	@Override
	public int queryMaxIdFromOrderSnapshot() {
		return orderSnapshotDao.queryMaxIdFromOrderSnapshot();
	}

	@Override
	public List<OrderSnapshot> queryForListByMaxId(int maxId) {
		return orderSnapshotDao.queryForListByMaxId(maxId);
	}

	@Override
	public boolean batchInsertOrderSnapshot(List<OrderSnapshot> orderSnapshots) {
		return orderSnapshotDao.batchInsertOrderSnapshot(orderSnapshots);
	}

	@Override
	public boolean updateOrderSnapshot(OrderSnapshot orderSnapshot) {
		return orderSnapshotDao.updateOrderSnapshot(orderSnapshot);
	}

	

}
