local gpuID = "right"
local kbID = "top"
local hudSide = "left"

local gpu = peripheral.wrap(gpuID)
if not gpu then
    error("GPU not found: "..gpuID)
end

local kb = peripheral.wrap(kbID)
if not kb then
    error("Keyboard not found: "..kbID)
end

local hud = peripheral.wrap(hudSide)
if not hud or peripheral.getType(hudSide) ~= "monitor" then
    error("HUD monitor not found on "..hudSide)
end

local mapWidth, mapHeight = 128, 128

gpu.refreshSize()
local monitorWidth, monitorHeight = gpu.getSize()
gpu.fill()
gpu.sync()
sleep(0.1)

hud.setTextScale(1)
hud.clear()

local mapId = 1

local function renderHUD(id, mapColors)
    hud.setTextScale(2)
    hud.clear()
    hud.setCursorPos(2, 2)
    hud.setBackgroundColor(colors.black)
    hud.setTextColor(colors.white)
    hud.write("Map: "..id)
    if not mapColors then
        hud.setCursorPos(2, 4)
        hud.setTextColor(colors.red)
        hud.write("Invalid")
        hud.setCursorPos(4, 5)
        hud.write("map!")
    end
end

local function renderMap(id)
    local mapColors = maps.getMapColors("overworld", id)

    local scaleX = monitorWidth / mapWidth
    local scaleY = monitorHeight / mapHeight

    for x = 1, mapWidth do
        for y = 1, mapHeight do
            local color
            if mapColors then
                color = mapColors[x][y]
            else
                color = 0
            end

            local startX = math.floor((x-1)*scaleX)+1
            local startY = math.floor((y-1)*scaleY)+1
            local endX = math.floor(x*scaleX)
            local endY = math.floor(y*scaleY)
            gpu.filledRectangle(startX, startY, endX, endY, color)
        end
    end

    gpu.sync()
    renderHUD(id, mapColors)
end

renderMap(mapId)

local function handleKeyboard()
    while true do
        local event, per, key, code, player = os.pullEvent()
        if (event == "tm_keyboard_key") and (per == kbID) then
            if key == keys.up then
                mapId = mapId + 1
                renderMap(mapId)
            elseif key == keys.down then
                mapId = math.max(1, mapId - 1)
                renderMap(mapId)
            end
        end
    end
end

handleKeyboard()
