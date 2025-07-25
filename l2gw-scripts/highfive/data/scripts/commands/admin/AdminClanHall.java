package commands.admin;

import ru.l2gw.extensions.scripts.Functions;
import ru.l2gw.gameserver.cache.Msg;
import ru.l2gw.gameserver.handler.AdminCommandDescription;
import ru.l2gw.gameserver.instancemanager.AuctionManager;
import ru.l2gw.gameserver.instancemanager.ResidenceManager;
import ru.l2gw.gameserver.model.L2Clan;
import ru.l2gw.gameserver.model.L2Object;
import ru.l2gw.gameserver.model.L2Player;
import ru.l2gw.gameserver.model.entity.ClanHall;
import ru.l2gw.gameserver.model.entity.siege.SiegeUnit;
import ru.l2gw.gameserver.model.gmaccess.AdminTemplateManager;
import ru.l2gw.gameserver.model.instances.L2DoorInstance;
import ru.l2gw.gameserver.model.zone.L2Zone;
import ru.l2gw.gameserver.serverpackets.NpcHtmlMessage;
import ru.l2gw.gameserver.tables.ClanTable;

public class AdminClanHall extends AdminBase
{
	private static AdminCommandDescription[] _adminCommands =
			{
					new AdminCommandDescription("admin_clanhall", null),
					new AdminCommandDescription("admin_clanhallset", null),
					new AdminCommandDescription("admin_clanhalldel", null),
					new AdminCommandDescription("admin_clanhallopendoors", null),
					new AdminCommandDescription("admin_clanhallclosedoors", null),
					new AdminCommandDescription("admin_clanhallteleportself", null)
			};

	public boolean useAdminCommand(String command, String[] args, String fullCommand, L2Player activeChar)
	{
		SiegeUnit clanhall = null;
		
		if(!AdminTemplateManager.checkCommand(command, activeChar, null, args.length > 0 ? args[0] : null, null, null))
		{
			Functions.sendSysMessage(activeChar, "Access denied.");
			return false;
		}
		
		if(args.length > 0)
			clanhall = ResidenceManager.getInstance().getBuildingById(Integer.parseInt(args[0]));
		
		if(command.startsWith("admin_clanhall"))
		{
			if(clanhall == null)
				showClanHallSelectPage(activeChar);
			else if(command.equalsIgnoreCase("admin_clanhall"))
				showClanHallPage(activeChar, clanhall);
			else
			{
				L2Object target = activeChar.getTarget();
				L2Player player = null;
				if(target != null)
				{
					if(target.isPlayer())
						player = (L2Player) target;
				}
				else
					player = activeChar;

				if(command.equalsIgnoreCase("admin_clanhallset"))
				{
					if(player == null || player.getClanId() == 0)
						activeChar.sendPacket(Msg.THAT_IS_THE_INCORRECT_TARGET);
					else
					{
						clanhall.changeOwner(player.getClanId());
						AuctionManager.getInstance().removeAuction((ClanHall) clanhall);
						System.out.println("ClanHall " + clanhall.getName() + "(id: " + clanhall.getId() + ") owned by clan " + player.getClan().getName());
					}
				}
				else if(command.equalsIgnoreCase("admin_clanhalldel"))
				{
					clanhall.changeOwner(0);
					AuctionManager.getInstance().addToAuction((ClanHall) clanhall);
				}
				else if(command.equalsIgnoreCase("admin_clanhallopendoors"))
				{
					for(L2DoorInstance door : clanhall.getDoors())
						if(door != null)
							clanhall.openCloseDoor(activeChar, door.getDoorId(), true);
				}
				else if(command.equalsIgnoreCase("admin_clanhallclosedoors"))
				{
					for(L2DoorInstance door : clanhall.getDoors())
						if(door != null)
							clanhall.openCloseDoor(activeChar, door.getDoorId(), false);
				}
				else if(command.equalsIgnoreCase("admin_clanhallteleportself"))
				{
					L2Zone zone = clanhall.getZone();
					if(zone != null)
						activeChar.teleToLocation(zone.getSpawn());
				}
			}
		}
		return true;
	}

	public void showClanHallSelectPage(L2Player activeChar)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);

		StringBuffer replyMSG = new StringBuffer("<html><body>");
		replyMSG.append("<table width=268><tr>");
		replyMSG.append("<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("<td width=180><center><font color=\"LEVEL\">Clan Halls:</font></center></td>");
		replyMSG.append("<td width=40><button value=\"Back\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("</tr></table><br>");

		replyMSG.append("<table width=268>");
		replyMSG.append("<tr><td width=130>ClanHall Name</td><td width=58>Town</td><td width=80>Owner</td></tr>");

		// TODO: make sort by Location
		for(ClanHall clanhall : ResidenceManager.getInstance().getClanHallList())
			if(clanhall != null && clanhall.getSiegeZone() == null)
			{
				replyMSG.append("<tr><td>");
				replyMSG.append("<a action=\"bypass -h admin_clanhall " + clanhall.getId() + "\">" + clanhall.getName() + "</a>");
				replyMSG.append("</td><td>" + clanhall.getLocation() + "</td><td>");

				L2Clan owner = clanhall.getOwnerId() == 0 ? null : ClanTable.getInstance().getClan(clanhall.getOwnerId());
				if(owner == null)
					replyMSG.append("none");
				else
					replyMSG.append(owner.getName());

				replyMSG.append("</td></tr>");
			}

		replyMSG.append("</table>");
		replyMSG.append("</body></html>");

		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}

	public void showClanHallPage(L2Player activeChar, SiegeUnit clanhall)
	{
		NpcHtmlMessage adminReply = new NpcHtmlMessage(5);
		StringBuffer replyMSG = new StringBuffer("<html><body>");
		replyMSG.append("<table width=260><tr>");
		replyMSG.append("<td width=40><button value=\"Main\" action=\"bypass -h admin_admin\" width=40 height=15 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("<td width=180><center>ClanHall Name</center></td>");
		replyMSG.append("<td width=40><button value=\"Back\" action=\"bypass -h admin_clanhall\" width=40 height=15 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("</tr></table>");
		replyMSG.append("<center>");
		replyMSG.append("<br><br><br>ClanHall: " + clanhall.getName() + "<br>");
		replyMSG.append("Location: " + clanhall.getLocation() + "<br>");
		replyMSG.append("ClanHall Owner: ");
		L2Clan owner = clanhall.getOwnerId() == 0 ? null : ClanTable.getInstance().getClan(clanhall.getOwnerId());
		if(owner == null)
			replyMSG.append("none");
		else
			replyMSG.append(owner.getName());

		replyMSG.append("<br><br><br>");
		replyMSG.append("<table>");
		replyMSG.append("<tr><td><button value=\"Open Doors\" action=\"bypass -h admin_clanhallopendoors " + clanhall.getId() + "\" width=80 height=15 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("<td><button value=\"Close Doors\" action=\"bypass -h admin_clanhallclosedoors " + clanhall.getId() + "\" width=80 height=15 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\"></td></tr>");
		replyMSG.append("</table>");
		replyMSG.append("<br>");
		replyMSG.append("<table>");
		replyMSG.append("<tr><td><button value=\"Give ClanHall\" action=\"bypass -h admin_clanhallset " + clanhall.getId() + "\" width=80 height=15 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\"></td>");
		replyMSG.append("<td><button value=\"Take ClanHall\" action=\"bypass -h admin_clanhalldel " + clanhall.getId() + "\" width=80 height=15 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\"></td></tr>");
		replyMSG.append("</table>");
		replyMSG.append("<br>");
		replyMSG.append("<table><tr>");
		replyMSG.append("<td><button value=\"Teleport self\" action=\"bypass -h admin_clanhallteleportself " + clanhall.getId() + " \" width=80 height=15 back=\"L2UI_CT1.Button_DF\" fore=\"L2UI_CT1.Button_DF\"></td></tr>");
		replyMSG.append("</table>");
		replyMSG.append("</center>");
		replyMSG.append("</body></html>");

		adminReply.setHtml(replyMSG.toString());
		activeChar.sendPacket(adminReply);
	}

	public AdminCommandDescription[] getAdminCommandList()
	{
		return _adminCommands;
	}
}