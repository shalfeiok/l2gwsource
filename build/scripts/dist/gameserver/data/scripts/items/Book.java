package items;

import ru.l2gw.extensions.scripts.Functions;
import ru.l2gw.extensions.scripts.ScriptFile;
import ru.l2gw.gameserver.handler.IItemHandler;
import ru.l2gw.gameserver.handler.ItemHandler;
import ru.l2gw.gameserver.model.L2Playable;
import ru.l2gw.gameserver.model.L2Player;
import ru.l2gw.gameserver.model.instances.L2ItemInstance;
import ru.l2gw.gameserver.serverpackets.RadarControl;
import ru.l2gw.util.Location;

public class Book implements IItemHandler, ScriptFile
{
	private static final int[] _itemIds = {
			5588,
			6317,
			7561,
			7063,
			7064,
			7065,
			7066,
			7082,
			7083,
			7084,
			7085,
			7086,
			7087,
			7088,
			7089,
			7090,
			7091,
			7092,
			7093,
			7094,
			7095,
			7096,
			7097,
			7098,
			7099,
			7100,
			7101,
			7102,
			7103,
			7104,
			7105,
			7106,
			7107,
			7108,
			7109,
			7110,
			7111,
			7112,
			13130,
			13131,
			13132,
			13133,
			13134,
			13135,
			13136
	};

	public boolean useItem(L2Playable playable, L2ItemInstance item)
	{
		if(!playable.isPlayer())
			return false;

		L2Player activeChar = (L2Player) playable;
		Functions.show("data/html/help/" + item.getItemId() + ".htm", activeChar);
		if(item.getItemId() == 7063)
			activeChar.sendPacket(new RadarControl(0, 2, new Location(51995, -51265, -3104)));
		activeChar.sendActionFailed();
		return true;
	}

	public int[] getItemIds()
	{
		return _itemIds;
	}

	public void onLoad()
	{
		ItemHandler.getInstance().registerItemHandler(this);
	}

	public void onReload()
	{
	}

	public void onShutdown()
	{
	}
}