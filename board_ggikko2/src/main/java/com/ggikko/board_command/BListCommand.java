package com.ggikko.board_command;

import java.util.ArrayList;

import org.springframework.ui.Model;

import com.ggikko.board_dao.BDao;
import com.ggikko.board_dto.BDto;

public class BListCommand implements BCommand {

	@Override
	public void execute(Model model) {

		BDao dao = new BDao();
		ArrayList<BDto> dtos = dao.list();

		model.addAttribute("list", dtos);

	}

}
