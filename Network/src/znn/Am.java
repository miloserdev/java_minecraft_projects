package znn;
class Am {
	new BukkitRunnable() {
		private int i = 0;
		
		@Override
		public void run() {
			for (Player all : Bukkit.getOnlinePlayers()) {
				List<String> asd = Main.getPlugin().getConfig().getStringList("groups." + getGroup(all));
				if (!asd.isEmpty()) {
					for (; i >= asd.size(); i = 0);
					String prefix = asd.get(i).split(",,")[0];
					String suffix = asd.get(i).split(",,")[1];
					NMS.sendTag(all, prefix, suffix, 2);
					++i;
					System.out.println(i);
				}
			}
		}
	}.runTaskTimer(Main.getPlugin(), 0, 10);
}