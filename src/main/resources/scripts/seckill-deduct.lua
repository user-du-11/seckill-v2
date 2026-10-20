if redis.call("GET", KEYS[1]) == false then
	return -1
end
local stock = tonumber(redis.call("GET", KEYS[1]))
if stock <= 0 then
	return 0
end
return redis.call("DECR", KEYS[1])